const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            if (entry.target.dataset.src)
                entry.target.src = entry.target.dataset.src
            entry.target.dataset.src = ''
            observer.unobserve(entry.target)
        }
    })
})

export default {
    mounted(el, binding) {
        if (!el.src)
            el.src = 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fblank.png'
        el.dataset.src = binding.value
        observer.observe(el)
    },
    beforeUpdate(el, binding) {
        if (!el.dataset.src)
            el.src = binding.value
        else
            el.dataset.src = binding.value
    }
}
