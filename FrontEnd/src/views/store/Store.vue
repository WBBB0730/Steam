<template>
  <div class="app">
    <div class="app-background"></div>
    <div class="store-background"
         :class="{ 'store-background_day': currentHour >= 6 && currentHour < 18, 'store-background_night': currentHour < 6 || currentHour >= 18 }"></div>
    <!-- 导航栏 -->
    <div class="store-header">
      <RouterLink v-if="token" class="wishlist" to="wishlist">愿望单<span v-if="wishlistSize > 0"> ({{ wishlistSize }})</span></RouterLink>
      <div v-else class="wishlist-area"/>
      <div class="store-nav">
        <RouterLink class="store-nav-tab" to="">您的商店</RouterLink>
        <RouterLink class="store-nav-tab" to="">新鲜推荐</RouterLink>
        <RouterLink class="store-nav-tab" to="">类别</RouterLink>
        <RouterLink class="store-nav-tab" to="">点数商店</RouterLink>
        <RouterLink class="store-nav-tab" to="">新闻</RouterLink>
        <RouterLink class="store-nav-tab" to="">实验室</RouterLink>
        <div class="search">
          <input v-model="keyword" class="search-input" placeholder="搜索" @input="getSearchSuggestionsDebounce()">
          <RouterLink class="search-button" to=""/>
          <!-- 搜索建议 -->
          <div class="search-suggestions">
            <RouterLink v-for="(item, index) in suggestions" :key="index" class="search-suggestion" :to="`/app/${item.appId}`">
              <div class="search-suggestion-image">
                <img :src="item.header" alt="">
              </div>
              <div class="search-suggestion-info">
                <div class="search-suggestion-name">{{ item.name }}</div>
                <div class="search-suggestion-price">{{ getPriceStr(item.finalPrice) }}</div>
              </div>
              <div v-if="item.status === 1" class="search-suggestion-on-wishlist">
                <img src="@/assets/on_wishlist.png" alt="">
                <span>已在愿望单中</span>
              </div>
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
    <!-- 精选与推荐 -->
    <div class="store-section">
      <div class="store-section-title">精选与推荐</div>
      <Swiper class="recommendations" :num="recommendations.length">
        <template v-for="(item, index) in recommendations" :key="index" #[index]>
          <RouterLink class="recommendation" :to="`/app/${item.appId}`"
                      @mouseenter="recommendationHovered = true" @mouseleave="recommendationHovered = false">
            <div class="recommendation-cover">
              <img class="current" v-lazy="item.cover" alt="">
              <template v-for="(item, index) in item.images" :key="index">
                <img :class="{ current: recommendationImageIndex === index }" v-lazy="item" alt="">
              </template>
              <ExpandButton v-if="token" v-model:status="item.status" :app-id="item.appId"
                            :show="recommendationHovered" @update:status="getWishlistSize()"/>
              <div class="recommendation-on-wishlist" :class="{ show: item.status === 1 }">
                <img src="@/assets/on_wishlist.png" alt="">
                <span>已在愿望单中</span>
              </div>
            </div>
            <div class="recommendation-info">
              <div v-trunc class="recommendation-name">{{ item.name }}</div>
              <div class="recommendation-images">
                <div v-for="(item, index) in item.images.slice(0, 4)" :key="index" class="recommendation-image"
                     @mouseenter="recommendationImageIndex = index" @mouseleave="recommendationImageIndex = -1">
                  <img v-lazy="item" alt="">
                </div>
              </div>
              <div>
                <div class="recommendation-reason">现已推出</div>
                <div class="recommendation-tags">
                  <div class="recommendation-tag">热销商品</div>
                </div>
              </div>
              <div class="recommendation-price-area">
                <div v-if="item.discount === 0" class="recommendation-price">{{ getPriceStr(item.price) }}</div>
                <div v-else class="recommendation-price_discounted">
                  <span class="recommendation-discount">{{ getDiscountStr(item.discount) }}</span>
                  <span class="recommendation-origin-price">{{ getPriceStr(item.price) }}</span>
                  <span class="recommendation-final-price">{{ getPriceStr(item.finalPrice) }}</span>
                </div>
                <div class="recommendation-platforms">
                  <div v-if="item.win" class="recommendation-platform" title="Windows">
                    <img src="@/assets/icon_platform_win.png" alt="">
                  </div>
                  <div v-if="item.mac" class="recommendation-platform" title="MacOS">
                    <img src="@/assets/icon_platform_mac.png" alt="">
                  </div>
                  <div v-if="item.linux" class="recommendation-platform" title="Linux">
                    <img src="@/assets/icon_platform_linux.png" alt="">
                  </div>
                </div>
              </div>
            </div>
          </RouterLink>
        </template>
      </Swiper>
    </div>
    <!-- 广告 -->
    <div>

    </div>
    <!-- 特别优惠 -->
    <div>

    </div>
    <!-- 按类别浏览 -->
    <!--    <div class="store-section">-->
    <!--      <div class="store-section-title">按类别浏览</div>-->
    <!--      <Swiper class="categories">-->
    <!--        <template>-->
    <!--          <div>-->
    <!--            <div>开放世界</div>-->
    <!--            <div>科幻及赛博朋克</div>-->
    <!--          </div>-->
    <!--        </template>-->
    <!--      </Swiper>-->
    <!--    </div>-->
    <!-- 浏览Steam -->
    <div class="store-section">
      <div class="store-section-title">浏览 STEAM</div>
      <div class="browses">
        <RouterLink class="browse" to="">新品</RouterLink>
        <RouterLink class="browse" to="">优惠</RouterLink>
        <RouterLink class="browse" to="">免费游戏</RouterLink>
        <RouterLink class="browse" to="">按用户标签</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import Swiper from '@/components/Swiper.vue'
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import { getSearchSuggestionsApi, getRecommendationsApi } from '@/api/app'
import ExpandButton from '@/components/ExpandButton.vue'
import { debounce } from '@/utils/debounce'
import { getDiscountStr, getPriceStr } from '@/utils/format'
import { getWishlistSizeApi } from '@/api/wishlist'

const currentHour = ref(new Date().getHours())
const wishlistSize = ref(0)
const keyword = ref('')
const suggestions = ref([])
const recommendations = ref([])
const specials = ref([])

const recommendationImageIndex = ref(-1)
const recommendationHovered = ref(false)

const store = useStore()
const token = computed(() => store.getters['user/token'])

onMounted(() => {
  if (token.value)
    getWishlistSize()
  getRecommendations()
})

/** 获取愿望单物品数量 */
function getWishlistSize() {
  getWishlistSizeApi().then(({ data }) => {
    wishlistSize.value = data
  })
}

/** 防抖+获取搜索建议 */
const getSearchSuggestionsDebounce = debounce(getSearchSuggestions, 1000)

/** 获取搜索建议 */
function getSearchSuggestions() {
  if(keyword.value.length === 0) {
    suggestions.value = []
    return
  }
  getSearchSuggestionsApi(keyword.value).then(({ data }) => {
    suggestions.value = data
  })
}

/** 获取推荐列表 */
function getRecommendations() {
  getRecommendationsApi().then(({ data }) => {
    recommendations.value = [...data]
  })
}

</script>

<style scoped lang="scss">
.app {
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: max(calc(100vh - 104px), 765px);
  font-family: "Motiva Sans", sans-serif;
  overflow: hidden;
}

.app-background {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  z-index: -2;
  height: 100%;
  background-image: url("@/assets/Steam_Summer_Sale_pattern-04-day_lighter.gif");
  background-repeat: repeat;
}

.store-background {
  position: absolute;
  left: 0;
  right: 0;
  height: 765px;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -1;
}

.store-background_day {
  background-image: url("@/assets/home_header_bg_day_schinese.gif");
}

.store-background_night {
  background-image: url("@/assets/home_header_bg_night_schinese.gif");
}

.store-header {
  display: flex;
  flex-direction: column;
  width: 940px;
  margin: 7px auto 550px auto;
}

.wishlist-area {
  height: 20px;
  margin-bottom: 4px;
}

.wishlist {
  width: max-content;
  min-width: 50px;
  height: 20px;
  padding: 0 25px;
  margin: 0 0 4px auto;
  color: #ffffff;
  font-size: 11px;
  font-family: Arial, Helvetica, sans-serif;
  text-decoration: none;
  text-align: center;
  line-height: 20px;
  background-color: #6e96a0;
  background-image: url("@/assets/background_wishlist.jpg");
  background-size: cover;
  box-shadow: 0 0 3px #000000;

  &:hover {
    color: #111111;
    background-image: linear-gradient(135deg, #ffffff, rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.3));
  }
}

.store-nav {
  display: flex;
  align-items: center;
  height: 35px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.4);
  background: linear-gradient(90deg, rgba(62, 103, 150, 0.919) 11.38%, rgba(58, 120, 177, 0.8) 25.23%, rgb(15, 33, 110) 100%);
}

.store-nav-tab {
  height: 35px;
  padding: 0 15px;
  color: #e5e5e5;
  font-size: 13px;
  font-weight: bold;
  font-family: Arial, Helvetica, sans-serif;
  text-decoration: none;
  text-shadow: 0 2px 3px rgba(0, 0, 0, 0.3);
  line-height: 35px;

  &:hover {
    color: #ffffff;
    background: linear-gradient(90deg, rgba(33, 162, 255, 0.25) 0%, rgba(33, 162, 255, 0.15) 50%, rgba(50, 50, 51, 0) 100%);
  }
}

.search {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 202px;
  box-sizing: border-box;
  padding-left: 12px;
  border: 1px solid rgba(0, 0, 0, 0.3);
  border-radius: 3px;
  margin: 0 4px 0 auto;
  background-color: #316282;

  &:hover {
    color: #0e1c25;
    border-color: #4c9acc;
  }
}

.search-input {
  width: 0;
  flex-grow: 1;
  box-sizing: border-box;
  margin-right: 8px;
  border: none;
  outline: none;
  background: none;
  color: #ffffff;
  font-size: 13px;

  &:focus::placeholder {
    opacity: 0;
  }

  &::placeholder {
    color: #0e1c25;
    font-size: 14px;
    font-style: italic;
  }
}

.search-button {
  position: relative;
  flex-shrink: 0;
  width: 25px;
  height: 25px;
  margin: 2px;
  border-radius: 3px;
  overflow: hidden;
  background-image: url("@/assets/search_icon_btn_over.png");
  background-size: cover;

  &:hover::before {
    opacity: 0;
  }

  &::before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    z-index: 1;
    width: 100%;
    height: 100%;
    background-color: #316282;
    background-image: url("@/assets/search_icon_btn.png");
    background-size: cover;
  }
}

.search-suggestions {
  position: absolute;
  right: 0;
  top: 100%;
  box-shadow: 0 0 12px #000000;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s;
}

.search-input:focus ~ .search-suggestions {
  opacity: 1;
  pointer-events: auto;
}

.search-suggestion {
  width: 400px;
  display: inline-flex;
  gap: 8px;
  padding: 4px 8px;
  color: #f5f5f5;
  background-color: #3d4450;
  text-decoration: none;

  &:hover {
    color: #151515;
    background-color: #dcdedf;
  }
}

.search-suggestion-image {
  width: 130px;
  height: 61px;

  & > img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.search-suggestion-info {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.search-suggestion-name {
  font-size: 14px;
  font-weight: bold;
}

.search-suggestion-price {
  font-size: 13px;
}

.search-suggestion-on-wishlist {
  position: absolute;
  left: 4px;
  top: 50%;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 0 4px 4px;
  box-shadow: 0 0 6px 0 #000000;
  color: #111111;
  background-color: #d3deea;
  font-size: 11px;
  line-height: 1;
  transform: translateY(-50%);
  transition: opacity 0.2s, left 0.2s;

  & > span {
    position: relative;
    width: 0;
    white-space: nowrap;
    overflow: hidden;
    transition: width 0.2s;
  }
}

.search-suggestion:hover .search-suggestion-on-wishlist > span {
  width: 72px;
}

.store-section {
  align-self: center;
  width: 940px;
  margin-bottom: 50px;
}

.store-section-title {
  margin-bottom: 12px;
  color: #ffffff;
  font-size: 14px;
}

.recommendations {
  height: 353px;
  box-shadow: 0 0 7px 0 #000000;
}

.recommendation {
  display: flex;
  width: 940px;
  height: 353px;
  background-color: #000000;
  text-decoration: none;
}

.recommendation-cover {
  position: relative;
  box-shadow: 0 0 10px 5px #000000;
  width: 616px;
  height: 353px;
  overflow: hidden;

  & > img {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    background-color: #000000;
    opacity: 0;
    transition: opacity 0.2s;
    pointer-events: none;

    &.current {
      opacity: 1;
      transition: opacity 0s;
      pointer-events: auto;
    }
  }
}

.recommendation-on-wishlist {
  position: absolute;
  left: -50px;
  top: 28px;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px;
  box-shadow: 0 0 6px 0 #000000;
  color: #111111;
  background-color: #d3deea;
  font-size: 11px;
  line-height: 1;
  opacity: 0;
  transition: opacity 0.2s, left 0.2s;

  &.show {
    left: 0;
    opacity: 1;
  }
}

.recommendation-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
  box-sizing: border-box;
  width: 324px;
  height: 353px;
  padding: 0 12px 12px 12px;
  background: url("@/assets/background_maincap_2.jpg") right no-repeat;
}

.recommendation-name {
  display: flex;
  align-items: center;
  height: 69px;
  color: #ffffff;
  font-size: 24px;
  font-weight: lighter;
  text-overflow: ellipsis;
}

.recommendation-images {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 12px;
}

.recommendation-image {
  position: relative;
  height: 69px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  &::after {
    content: "";
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.2);
  }

  &:hover::after {
    background-color: transparent;
  }
}

.recommendation-reason {
  margin-bottom: 4px;
  color: #ffffff;
  font-size: 21px;
  font-weight: lighter;
}

.recommendation-tags {
  display: flex;
  flex-flow: wrap;
  row-gap: 4px;
  column-gap: 2px;
}

.recommendation-tag {
  padding: 0 7px;
  border-radius: 2px;
  color: #ffffff;
  background-color: rgba(255, 255, 255, 0.2);
  font-size: 11px;
  line-height: 20px;
  white-space: nowrap;
}

.recommendation-price-area {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.recommendation-price {
  color: #ffffff;
  font-size: 11px;
}

.recommendation-price_discounted {
  display: flex;
  height: 16px;
}

.recommendation-discount {
  padding: 0 4px;
  color: #beee11;
  background-color: #4c6b22;
  font-size: 12px;
  font-weight: bold;
}

.recommendation-origin-price {
  position: relative;
  padding: 0 4px;
  color: #738895;
  background-color: #344654;
  font-size: 11px;
  line-height: 16px;

  &::after {
    content: "";
    position: absolute;
    left: 6px;
    right: 4px;
    top: 50%;
    height: 1px;
    background-color: #738895;
    box-shadow: 0 0 2px #000000;
    transform: rotate(-8deg);
  }
}

.recommendation-final-price {
  padding-right: 4px;
  color: #beee11;
  background-color: #344654;
  font-size: 11px;
  line-height: 16px;
}

.recommendation-platforms {
  display: flex;
  opacity: 0.7;
}

.recommendation-platform {
  width: 20px;
  height: 20px;

  img {
    width: 100%;
    height: 100%;
  }
}

.browses {
  align-self: center;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  grid-gap: 8px;
  width: 940px;
}

.browse {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 58px;
  border-radius: 2px;
  color: #ffffff;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  font-size: 16px;
  text-decoration: none;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}
</style>
