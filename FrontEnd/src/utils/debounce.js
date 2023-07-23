/**
 * 防抖函数
 * @param func 执行函数
 * @param delay 防抖间隔
 * @returns {function} 封装了防抖功能的执行函数
 */
export function debounce(func, delay) {
    let timer
    return function () {
        clearTimeout(timer)
        timer = setTimeout(() => {
            func.apply(this, arguments)
        }, delay)
    }
}
