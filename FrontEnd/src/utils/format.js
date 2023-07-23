export function getDiscountStr(discount) {
    return '-' + Math.round(discount * 100) + '%'
}

export function getPriceStr(price) {
    return price === 0 ? '免费开玩' : '￥' + price.toFixed(2)
}

/**
 * 格式化日期
 * 年：YYYY/YY，月：MM/M，日：DD/D
 * @param time      时间戳
 * @param format1   完整时间格式
 * @param format2   年份相同时的时间格式
 * @returns {string}
 */
export function getDateStr(time, format1 = 'YYYY-MM-DD', format2 = '') {
    const date = new Date(time), now = new Date()
    const y = date.getFullYear(), m = date.getMonth() + 1, d = date.getDate()
    const str = (format2.length > 0 && y === now.getFullYear()) ? format2 : format1
    return str.replaceAll('YYYY', y.toString())
        .replaceAll('YY', y.toString().slice(-2))
        .replaceAll('MM', ('0' + m).slice(-2))
        .replaceAll('M', m.toString())
        .replaceAll('DD', ('0' + d).slice(-2))
        .replaceAll('D', d.toString())
}

/**
 * 格式化时间
 * 年：YYYY/YY，月：MM/M，日：DD/D，时：hh/h，分：mm/m，秒：ss/s
 * @param time      时间戳
 * @param format1   完整时间格式
 * @param format2   年份相同时的时间格式
 * @param format3   日期相同时的时间格式
 * @returns {string}
 */
export function getTimeStr(time, format1 = 'YYYY-MM-DD hh:mm:ss', format2 = '', format3 = '') {
    const date = new Date(time), now = new Date()
    const Y = date.getFullYear(), M = date.getMonth() + 1, D = date.getDate(),
        h = date.getHours(), m = date.getMinutes(), s = date.getSeconds()
    const str = (format3.length > 0 && Y === now.getFullYear() && M === now.getMonth() && D === now.getSeconds()) ? format3 :
        ((format2.length > 0 && Y === now.getFullYear()) ? format2 : format1)
    return str.replaceAll('YYYY', Y.toString())
        .replaceAll('YY', Y.toString().slice(-2))
        .replaceAll('MM', ('0' + M).slice(-2))
        .replaceAll('M', M.toString())
        .replaceAll('DD', ('0' + D).slice(-2))
        .replaceAll('D', D.toString())
        .replaceAll('hh', ('0' + h).slice(-2))
        .replaceAll('h', h.toString())
        .replaceAll('mm', ('0' + m).slice(-2))
        .replaceAll('m', m.toString())
        .replaceAll('ss', ('0' + s).slice(-2))
        .replaceAll('s', s.toString())
}
