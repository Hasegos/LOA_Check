document.addEventListener("DOMContentLoaded", function() {
    const characterContainers  = document.querySelectorAll(".character-image-container");

    characterContainers .forEach((container) => {
        const checkBoxes = container.querySelectorAll(".checkbox");
        let index = 0;


        setInterval(() => {
            if(index < checkBoxes.length){
                checkBoxes[index].classList.add("checked");
                index++;
            }
        }, 10000)

    })
});