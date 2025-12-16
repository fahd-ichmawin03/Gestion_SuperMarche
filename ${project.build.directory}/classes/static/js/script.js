// Confirmation avant suppression
function confirmerSuppression(event, nomElement) {
    if (!confirm("Êtes-vous sûr de vouloir supprimer " + nomElement + " ? Cette action est irréversible.")) {
        event.preventDefault();
        return false;
    }
    return true;
}

// Validation du formulaire de stock
function validerStock() {
    let qte = document.getElementById("quantite").value;
    if (qte <= 0) {
        alert("La quantité doit être supérieure à 0 !");
        return false;
    }
    return true;
}

// Animation simple au chargement
document.addEventListener("DOMContentLoaded", function() {
    const cards = document.querySelectorAll('.card-custom');
    cards.forEach((card, index) => {
        card.style.opacity = 0;
        setTimeout(() => {
            card.style.transition = "opacity 0.5s ease-in";
            card.style.opacity = 1;
        }, index * 100);
    });
});