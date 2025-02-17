/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js}", "./node_modules/flowbite/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        light: {
          background: "#edf1f2",
          primary: "#121212",
          secondary: "#383e42",
          accent: "#43e5c5",
          text: "#040316",
        },
        dark: {
          background: "#0d1112",
          primary: "#ededed",
          secondary: "#bdc3c7",
          accent: "#1abc9c",
          text: "#eae9fc",
        },
      },
    },
  },
  plugins: [require('flowbite/plugin')],
  darkMode: "selector",
}

