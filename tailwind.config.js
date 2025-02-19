/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js}", "./node_modules/flowbite/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        light: {
          background: "#f1f1f1",
          background_secondary: "#ececec",
          primary: "#121212",
          primary_hover: "#040316",
          secondary: "#51575c",
          secondary_hover: "#41454A",
          accent: "#43e5c5",
          accent_hover: "#43f1c5",
          text: "#040316",
        },
        dark: {
          background: "#0d1112",
          background_secondary: "#232323",
          primary: "#ededed",
          primary_hover: "#EAE9FC",
          secondary: "#bdc3c7",
          secondary_hover: "#A8ACB2",
          accent: "#1abc9c",
          accent_hover: "#13936E",
          text: "#eae9fc",
        },
      },
    },
  },
  plugins: [require('flowbite/plugin')],
  darkMode: "selector",
}

