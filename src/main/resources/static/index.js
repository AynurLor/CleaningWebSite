let popup = document.querySelector(".popup");
let price = document.querySelector(".price");
let priceItem = price.querySelector(".price__item-1");
let priceItem2 = price.querySelector(".price__item-2");
let priceItem3 = price.querySelector(".price__item-3");
let priceItem4 = price.querySelector(".price__item-4");
let priceItem5 = price.querySelector(".price__item-5");
let priceItem6 = price.querySelector(".price__item-6");
let priceItem7 = price.querySelector(".price__item-7");
let priceItem8 = price.querySelector(".price__item-8");
let priceItem9 = price.querySelector(".price__item-9");
let body = document.querySelector(".page");
let formBtn = popup.querySelector(".popup__save-button");

priceItem.addEventListener("click", visible);
priceItem2.addEventListener("click", visible);
priceItem3.addEventListener("click", visible);
priceItem4.addEventListener("click", visible);
priceItem5.addEventListener("click", visible);
priceItem6.addEventListener("click", visible);
priceItem7.addEventListener("click", visible);
priceItem8.addEventListener("click", visible);
priceItem9.addEventListener("click", visible);

function visible() {
  popup.style.visibility = "visible";
}

function hidden(event) {
    event.preventDefault()
    popup.style.visibility = "hidden";
}

formBtn.addEventListener("click", hidden)