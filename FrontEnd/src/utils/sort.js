function qSort(list, comp, l, r) {
    if (r - l < 2)
        return
    const base = list[l]
    let i = l, j = r - 1
    while (i < j) {
        while (i < j && comp(base, list[j]))
            j--
        while (i < j && comp(list[i], base))
            i++
        if (i < j)
            [list[i], list[j]] = [list[j], list[i]]
    }
    [list[l], list[i]] = [list[i], list[l]]
    qSort(list, comp, l, i)
    qSort(list, comp, i + 1, r)
}

/**
 * 快速排序
 * @param list  待排序列表
 * @param comp  比较函数 comp(a, b)，返回a排在b之前是否合法
 */
export function quickSort(list, comp) {
    qSort(list, comp, 0, list.length)
}
