const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            entry.target.src = entry.target.dataset.src
            entry.target.dataset.src = null
            observer.unobserve(entry.target)
        }
    })
})

import('@/assets/blank.png')

export default {
    mounted(el, binding) {
        if (!el.src)
            el.src = 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fblank.png'
        el.dataset.src = binding.value
        observer.observe(el)
    }
}
