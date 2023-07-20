/**
 * 检验邮箱地址是否合法
 * @param email
 * @returns {boolean}
 */
export function isEmailValid(email) {
    return /^[\w-]+(.[\w-]+)*@([a-zA-Z0-9]+(-?[a-zA-Z0-9]+)+\.)+[a-zA-Z]{2,4}$/.test(email)
}

/**
 * 检验密码是否合法
 * @param password
 * @returns {boolean}
 */
export function isPasswordValid(password) {
    return password.length >= 8
}
