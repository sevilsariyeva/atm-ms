const enterCardBtn = document.querySelector("#enterCardBtn");
const registerBtn = document.querySelector("#registerBtn");
const enterHtml = document.querySelector("#enterHtml").innerHTML;
const registerHtml = document.querySelector("#registerHtml").innerHTML;
const pinHtml = document.querySelector("#pinHtml").innerHTML;
let submitCardNumBtn;

enterCardBtn.addEventListener("click", function () {
  document.querySelector("#mainDiv").innerHTML = enterHtml;
  submitCardNumBtn = document.querySelector("#submitCardNumBtn");
  submitCardNumBtn.addEventListener("click", checkCardNumber);
  document
    .getElementById("cardNumberInput")
    .addEventListener("input", function (event) {
      const input = event.target;
      if (input.value.length > 16) {
        input.value = input.value.slice(0, 16);
      }
    });
});
registerBtn.addEventListener("click", function () {
  document.querySelector("#mainDiv").innerHTML = registerHtml;
});

function checkCardNumber() {
  const cardNumber = document.querySelector("#cardNumberInput").value;

  fetch(`/atm/check-card-number?cardNumber=${cardNumber}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    }
  })
    .then((response) => response.json())
    .then((account) => {
      if (account && account.cardNumber) {
        document.querySelector("#mainDiv").innerHTML = pinHtml;

        document.querySelector("#submitPinBtn").addEventListener("click", function() {
          const enteredPin = document.querySelector("#pinInput").value;

          if (enteredPin.length !== 4) {
            alert("PIN must be exactly 4 digits.");
            return;
          }

          if (isNaN(enteredPin)) {
            alert("PIN must contain only digits.");
            return;
          }
          console.log(typeof account.pin);
          console.log(typeof enteredPin);
          if (account.pin === Number(enteredPin)) {
            alert("PIN is correct. Access granted.");
          } else {
            alert("Incorrect PIN. Please try again.");
          }
        });

      } else {
        alert("Card number not found. Please try again or register.");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}


