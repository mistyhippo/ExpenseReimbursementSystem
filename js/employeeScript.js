

function create4buttonStatus(id)
{
    let tableID = document.getElementById(id);
    let spanID = document.getElementById(id+1);
    tableID.style.display = 'block';
    spanID.textContent = id;

    //Display submit buttons (save and submit)
    document.getElementById('submit').style.display = 'block';
    //document.getElementById('buttons').style.display = 'block';
}

function changeFieldToVisible(){
    // document.getElementById('email').style.visibility = "visible";
    // document.getElementById('fname').style.visibility = "visible";
    // document.getElementById('lname').style.visibility = "visible";
    // document.getElementById('password').style.visibility = "visible";
    //let inputs = document.getElementsByTagName('input');
    let inputs = document.getElementsByClassName('ableToVisible');
    for(let i = 0; i<inputs.length; i++) {
        if(i == 0){
            inputs[i].disabled = true;
        }if(i == 4){
            inputs[i].disabled = true;
        }else{
            inputs[i].disabled = false;
        }

    }
}


//Load data into employee-index page
function loadDataIntoUserPage(res){
    let idForm = document.forms["userForm"]["id"];
    let fname = document.forms["userForm"]["fname"];
    let lname = document.forms["userForm"]["lname"];
    let email = document.forms["userForm"]["email"];
    let role = document.forms["userForm"]["role"];
    let password = document.forms["userForm"]["password"];

    idForm.value = res.userId;
    fname.value = res.firstName;
    lname.value = res.lastName;
    email.value = res.email;
    role.value = res.role;
    password.value = res.passWord;

    // if (x == "") {
    //     alert("Name must be filled out");
    //     return false;
    // }
}

//Make API call for user form
function getUserInfoIntoForm(){
    //Step 1:  Create the new XHR object
    let xhttp = new XMLHttpRequest();

    //Step 2: Create a callback function for readystatechange
    xhttp.onreadystatechange = getData = ()=> {
        if(xhttp.readyState === 4 && xhttp.status === 200){
            console.log("Info from getUerINfoIntoForm" + xhttp.responseText);
            let res = JSON.parse(xhttp.responseText);
            loadDataIntoUserPage(res);
        }
    }
    let apiUrl = `${URL}/user`

    //Step 3. Open the request
    xhttp.open('GET', apiUrl);
    xhttp.withCredentials = true;
    //Step 4. Send the request
    xhttp.send();
}

let submitUpdateProfile = async () => {
    //Send data to server
    let userId = document.getElementById('id').value;
    let firstName = document.getElementById('fname').value;
    let lastName = document.getElementById('lname').value;
    let email = document.getElementById('email').value;
    let role = document.getElementById('role').value;
    let passWord = document.getElementById('password').value;

   let emailValidate = isEmailValidity(email);
   let passwordValidate = isPasswordValidity(passWord);
   if(!emailValidate) {
       document.getElementById("updateEmailSpan").style.visibility = "visible";
   }

   if(!passwordValidate) {
       document.getElementById("updatePasswordSpan").style.visibility = "visible";

   }

   if(emailValidate == true && passwordValidate == true){
       alert("Profile updated !!!");
       let updateObj = {userId,firstName,lastName,email,role,passWord};
       let req = await fetch(`${URL}/user/update`, {
           credentials: "include",
           method: 'PUT',
           body: JSON.stringify(updateObj)
       });
   }

    // let userId = document.forms["userForm"]["id"].value;
    // let firstName = document.forms["userForm"]["fname"].value;
    // let lastName = document.forms["userForm"]["lname"].value;
    // let email = document.forms["userForm"]["email"].value;
    // let role = document.forms["userForm"]["role"].value;
    // let passWord = document.forms["userForm"]["password"].value;

}
let updateProfile = async () => {
    let inputs = document.getElementsByTagName('input');

    for(let i = 0; i<inputs.length; i++) {
        inputs[i].disabled = true;
    }
    submitUpdateProfile();
    // alert("Profile updated !!!")

};

isEmailValidity = (emailAddress) => {
    {
        if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(emailAddress))
        {
            return true;
        }else{
            alert("Invalid email address !!!")
            return false;
        }

    }
}

isPasswordValidity = (password) => {
    if(password.length > 7) {
        return true;
    }else{
        alert("Invalid Password !!!")
        return false;
    }
}
