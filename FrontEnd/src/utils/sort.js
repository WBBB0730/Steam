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

function getBinaryInsertIndex(list, item, comp, equal, l, r) {
    const mid = Math.floor((l + r - 1) / 2)
    const midItem = list[mid]
    if (l + 1 === r) {
        return equal(midItem, item) ? -1 : (comp(item, midItem) ? mid : mid + 1)
    }
    return comp(item, midItem) ? getBinaryInsertIndex(list, item, comp, equal, l, mid + 1) : getBinaryInsertIndex(list, item, comp, equal, mid + 1, r)
}

/**
 * 向有序列表中插入数据
 * @param list 有序列表
 * @param item 待插入数据
 * @param comp 比较函数 comp(a, b)，返回a排在b之前是否合法
 * @param equal 查重函数 equal(a, b)，返回a与b是否等价，若等价则不插入
 */
export function binaryInsert(list, item, comp, equal = (a, b) => false) {
    const insertIndex = list.length > 0 ? getBinaryInsertIndex(list, item, comp, equal, 0, list.length) : 0
    if (insertIndex !== -1)
        list.splice(insertIndex, 0, item)
}
