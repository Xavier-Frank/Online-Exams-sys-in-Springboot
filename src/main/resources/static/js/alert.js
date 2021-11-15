function Validate(){
var password= document.getElementById("password").value;
var confirmPassword= document.getElementById("confirm_password").value;
if(password != confirmPassword){
alert("Passwords do not match");
return false;
}
return true;
}

var check = function() {
  if (document.getElementById('password').value ==
    document.getElementById('confirm_password').value) {
    document.getElementById('message').style.color = 'green';
    document.getElementById('message').innerHTML = 'passwords match';
  } else {
    document.getElementById('message').style.color = 'red';
    document.getElementById('message').innerHTML = 'please match the passwords';
  }
}