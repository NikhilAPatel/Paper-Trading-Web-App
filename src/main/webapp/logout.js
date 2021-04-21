function goLogout(){
	//TODO
	//Go home and reset everything in session storage
	sessionStorage.clear();
	location.replace("index.html");
}