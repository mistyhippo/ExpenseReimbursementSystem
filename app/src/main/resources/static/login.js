//Cookie will look something like this
//cookies=pid=6;loggedIn=email;JSESSIONID=alsdkfhjasd09adslkjrhfq;nextcookie=klajsdlfja;

function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for(let i = 0; i <ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}


function login(){
    let email = document.getElementById('email').value;
    let password = document.getElementById('password').value;

    let loginObj = {
        email,
        password
    }

    fetch('http://localhost:7000/login', {
        method: 'POST',
        body: JSON.stringify(loginObj)
    })
    .then((res) => {
        console.log(res.headers.get('pid'));
        console.log(res.headers.get("loggedIn"));

        //This is how we would save the cookies on the browser
        document.cookie = `pid=${res.headers.get('pid')};`;
        document.cookie = `name=${res.headers.get('loggedIn')};`;
        setMessage();
        console.log(res);

    });
}
function logout(){
    fetch('http://localhost:7000/logout')
    .then(res => {
        document.cookie = 'name=;';
        document.cookie = 'pid=;';
        document.getElementById('message').innerText = 'Please login';
    })
}


let loginBtn = document.getElementById('login').addEventListener('click', login);
let logoutBtn = document.getElementById('logout').addEventListener('click', logout);

(setMessage = () => {
    if(getCookie("name")){
        document.getElementById('message').innerText = `Hello ${getCookie('name')}`;
    }
})();