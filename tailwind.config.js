/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js}", "./node_modules/flowbite/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        light: {
          background: "#F3F4F6",
          background_secondary: "#FFFFFF",
          background_tertiary: "#FAFAFA",
          primary: "#1A202C",
          primary_hover: "#040316",
          secondary: "#4A5568",
          secondary_hover: "#41454A",
          tertiary: "#718096",
          accent: "#2b6cb0",
          accent_hover: "#44f1c5",
        },
        dark: {
          background: "#1A202C",
          background_secondary: "#2D3748",
          background_tertiary: "#3A4558",
          primary: "#EDF2F7",
          primary_hover: "#EAE9FC",
          secondary: "#E2E8F0",
          secondary_hover: "#A8ACB2",
          tertiary: "#CBD5E0",
          accent: "#0600c2",
          accent_hover: "#0600ca",
        },
      },
    },
  },
  plugins: [require('flowbite/plugin')],
  darkMode: "selector",
}

