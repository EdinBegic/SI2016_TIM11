class AccountsController {
	static $inject = ['accountService'];

	constructor(accountService) {
		this.accountService = accountService;
		this.loadAccounts(1);
		this.setEmptyAccount();
	}

	registerAccount() {
		if (!this.form.$valid) {
			return;
		}

		this.accountService.create(this.account).then(response => {
			this.loadAccounts(1);
			this.resetForm();
		}, error => {
			console.log(JSON.stringify(error));
			// ovo je poseban slucaj koji nastaje 
			// kada se desi bacanje custom izuzetka na backendu
			if (error.status == "400") {
				this.error = { "error" : "Invalid request",
						      "message": error.data.message.string 
				};
			}
			else {
				this.error = error;
			}
		});
	}

	setEmptyAccount() {
		this.account = {fullName: "", username: "", email: "", password: "", roleId: 1};
	}

	loadAccounts(page) {
		this.accountService.getPage(page).then(response => {
			this.accounts = response.data.content;
			this.number = response.data.number + 1;
			this.totalPages = response.data.totalPages;
		});
	}

 	loadAllAccounts() {
        this.accountService.all().then(response => {
            this.allAccounts = response.data;
        });
    }

	goto(newPage) {
		if (newPage > 0 && newPage <= this.totalPages) {
			this.loadAccounts(newPage);
		}
	}

	edit(id) {}

	delete(id) {
		swal({
			title: 'Da li ste sigurni?',
			text: 'Obrisani korisnički se ne može vratiti nakon brisanja.',
			type: 'warning',
			showCancelButton: true,
			confirmButtonText: 'Da, obriši',
			cancelButtonText: 'Ne',
			closeOnConfirm: true
		}, () => {
			this.accountService.delete(id).then(response => {
				if (this.accounts.length > 1) {
					this.loadAccounts(this.number);
				} else if (this.totalPages > 1) {
					this.goto(this.number - 1);
				} else {
					this.accounts = [];
				}
			});
		});
	}

	resetForm() {
		this.form.$setPristine();
		this.form.$setUntouched();
		this.form.$submitted = false;
		this.setEmptyAccount();
	}
}

export default AccountsController;
