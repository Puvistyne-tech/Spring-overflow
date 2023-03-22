let form = document.querySelector('.to-validate');
form.addEventListener('submit',validate);

function validate(e){
    if(form.checkValidity()===false){
        e.preventDefault();
    }
    form.classList.add('was-validated');
}






