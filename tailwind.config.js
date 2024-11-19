/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js}", "./node_modules/flowbite/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [require('flowbite/plugin')],
  darkMode: "selector",
}

