/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  root: true,
  'extends': [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-prettier/skip-formatting'
  ],
  parserOptions: {
    ecmaVersion: 'latest'
  },
  rules: {
    'quotes': [1, 'single'],
    'object-curly-spacing': [1, 'always'],
    'semi': [1, 'never'],
    'vue/multi-word-component-names': [0],
    'vue/no-unused-vars': [1],
    'no-unused-vars': [1]
  }
}
