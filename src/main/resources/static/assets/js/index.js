const enterCardBtn = document.querySelector("#enterCardBtn");
const registerBtn = document.querySelector("#registerBtn");
const enterHtml = document.querySelector("#enterHtml").innerHTML;
const registerHtml = document.querySelector("#registerHtml").innerHTML;
const pinHtml = document.querySelector("#pinHtml").innerHTML;
const mainHtml = document.querySelector("#mainHtml").innerHTML;
const withdrawSuccess = document.querySelector("#withdrawSuccess").innerHTML;

function setupInitialListeners() {
  document.querySelector("#enterCardBtn").addEventListener("click", function () {
    document.querySelector("#mainDiv").innerHTML = enterHtml;
    setupCardNumberInput();
  });

  document.querySelector("#registerBtn").addEventListener("click", function () {
    document.querySelector("#mainDiv").innerHTML = registerHtml;

    document.querySelector("#register").addEventListener("click", function () {
      const fullname = document.querySelector("#fullname").value;
      const email = document.querySelector("#email").value;

      fetch("/atm/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ fullname, email }),
      })
        .then((response) => response.json())
        .then((account) => {
          if (account && account.cardNumber && account.pin) {
            document.querySelector("#mainDiv").innerHTML = withdrawSuccess;
            document.getElementById("cardNumberText").textContent = account.cardNumber;
            document.getElementById("pinText").textContent = account.pin;

            const returnToMainBtn = document.querySelector("#returnToMain");
            if (returnToMainBtn) {
              returnToMainBtn.addEventListener("click", function () {
                document.querySelector("#mainDiv").innerHTML = mainHtml;
                setupInitialListeners(); // Re-attach event listeners
              });
            }
          } else {
            alert("Registration failed. Please try again.");
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          alert("An error occurred: " + error.message);
        });
    });
  });
}

function setupCardNumberInput() {
  const submitCardNumBtn = document.querySelector("#submitCardNumBtn");
  if (submitCardNumBtn) {
    submitCardNumBtn.addEventListener("click", checkCardNumber);
  }

  const cardNumberInput = document.getElementById("cardNumberInput");
  if (cardNumberInput) {
    cardNumberInput.addEventListener("input", function (event) {
      const input = event.target;
      if (input.value.length > 16) {
        input.value = input.value.slice(0, 16);
      }
    });
  }
}

function checkCardNumber() {
  const cardNumber = document.querySelector("#cardNumberInput").value;

  fetch(`/atm/check-card-number?cardNumber=${cardNumber}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => response.json())
    .then((account) => {
      if (account && account.cardNumber) {
        document.querySelector("#mainDiv").innerHTML = pinHtml;
        setupPinInput(account);
      } else {
        alert("Card number not found. Please try again or register.");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

function setupPinInput(account) {
  const submitPinBtn = document.querySelector("#submitPinBtn");
  if (submitPinBtn) {
    submitPinBtn.addEventListener("click", function () {
      const enteredPin = document.querySelector("#pinInput").value;

      if (enteredPin.length !== 4) {
        alert("PIN must be exactly 4 digits.");
        return;
      }

      if (isNaN(enteredPin)) {
        alert("PIN must contain only digits.");
        return;
      }

      if (account.pin === Number(enteredPin)) {
        document.querySelector("#mainDiv").innerHTML = document.querySelector("#accountDetailsHtml").innerHTML;
        setupAccountPage(account);
      } else {
        alert("Incorrect PIN. Please try again.");
      }
    });
  }
}

function setupAccountPage(account) {
  document.querySelector("#accountName").textContent = account.fullname;
  document.querySelector("#accountBalance").textContent = account.balance;

  document.querySelector("#withdrawBtn").addEventListener("click", function () {
    document.querySelector("#mainDiv").innerHTML = document.querySelector("#withdrawHtml").innerHTML;

    document.querySelector("#submitWithdrawBtn").addEventListener("click", function () {
      const amount = document.querySelector("#withdrawAmount").value;

      if (amount && amount > 0) {
        executeTransaction("WITHDRAW", account.cardNumber, amount);
      } else {
        alert("Please enter a valid amount.");
      }
    });
  });

  document.querySelector("#depositBtn").addEventListener("click", function () {
    document.querySelector("#mainDiv").innerHTML = document.querySelector("#depositHtml").innerHTML;

    document.querySelector("#submitDepositBtn").addEventListener("click", function () {
      const amount = document.querySelector("#depositAmount").value;

      if (amount && amount > 0) {
        executeTransaction("DEPOSIT", account.cardNumber, amount);
      } else {
        alert("Please enter a valid amount.");
      }
    });
  });
}

function executeTransaction(type, cardNumber, amount) {
  fetch(`/transaction/execute/type/${type}/${cardNumber}/${amount}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.success) {
        alert(`${type} successful!`);
        document.querySelector("#mainDiv").innerHTML = mainHtml;
        setupInitialListeners(); // Re-attach event listeners
      } else {
        alert(`${type} failed: ${data.message}`);
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("An error occurred: " + error.message);
    });
}

setupInitialListeners();
