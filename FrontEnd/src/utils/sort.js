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

export function quickSort(list, comp) {
    qSort(list, comp, 0, list.length)
}
