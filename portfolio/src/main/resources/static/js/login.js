window.onload = () => {
	let input_login = document.getElementById("username");
	let input_pw = document.getElementById("password");
	
	input_login.addEventListener("click",() => {
		input_login.focus();
	})
	input_pw.addEventListener("click",() => {
		input_login.focus();
	})
}