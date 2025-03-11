const backToTopButton = document.createElement('button');
    backToTopButton.innerHTML = '<i class="bi bi-arrow-up"></i>';
    backToTopButton.classList.add('btn', 'btn-primary', 'back-to-top');
    document.body.appendChild(backToTopButton);

    window.addEventListener('scroll', () => {
        if (window.scrollY > 300) {
            backToTopButton.style.display = 'block';
        } else {
            backToTopButton.style.display = 'none';
        }
    });

    backToTopButton.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });