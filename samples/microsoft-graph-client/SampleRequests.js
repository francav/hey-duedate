const { client } = require("./clientInitialization/TokenCredentialAuthenticationProvider");

// Get the name of the authenticated user with promises
client
	.api("/users")
	.get()
	.then((res) => {
		console.log(res);
	})
	.catch((err) => {
		console.log(err);
	});