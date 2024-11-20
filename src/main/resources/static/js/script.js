//  for dark theme
let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    changeTheme(currentTheme);
})

function changeTheme(){
    //set to web page
    document.querySelector("html").classList.add(currentTheme);

    // set the listener to change theme button
    const changeThemeButton = document.querySelector('#theme_change_button');

    // change button text
    changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? "Dark" : "Light";

    changeThemeButton.addEventListener("click", () => {
        let oldTheme = currentTheme;
        if(currentTheme == "dark"){
            // theme to light
            currentTheme = "light";
        }else{
            // theme to dark
            currentTheme = "dark";
        }

        // update in local storage
        setTheme(currentTheme);
        
        // remove the current theme
        document.querySelector("html").classList.remove(oldTheme);
        // set the current theme
        document.querySelector("html").classList.add(currentTheme);

        // change button text
    changeThemeButton.querySelector("span").textContent = currentTheme == "light" ? "Dark" : "Light";
        
    })
}


// set theme to local storage
function setTheme(theme){
    localStorage.setItem("theme", theme);
}

// get theme from local storage
function getTheme(){
    let theme = localStorage.getItem("theme");
    if(theme) return theme;
    return "dark";
}