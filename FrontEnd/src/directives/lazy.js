const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach((entry) => {
        console.log(entry)
        if (entry.isIntersecting) {
            entry.target.src = entry.target.dataset.src
            entry.target.dataset.src = null
            observer.unobserve(entry.target)
        }
    })
})

export default {
    mounted(el, binding) {
        el.src = '/assets/blank.png'
        el.dataset.src = binding.value
        observer.observe(el)
    }
}
