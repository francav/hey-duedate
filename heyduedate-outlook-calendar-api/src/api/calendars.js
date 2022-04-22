import resource from 'resource-router-middleware';
import msGraphClient from '../ms-graph-client';
import calendarConverter from '../calendar-converter';

async function fetchEvents(userId, calendars) {
	let events = [];
	await Promise.all(calendars.map(async (calendar) => {
		await msGraphClient
			.api("users/" + userId + "/calendars/" + calendar.id + "/events?$orderby=start/dateTime&$select=subject,type,start,end&top=1000")
			.get()
			.then((response) => {
				events.push({
					userId: userId, calendarId: calendar.id, calendarName: calendar.name, events: response.value.map(o => {
						return {
							id: o.id,
							subject: o.subject,
							type: o.type,
							start: o.start,
							end: o.end
						}
					})
				});
			})
			.catch((error) => {
				console.log(error);
			});
	}));

	return events;
}

async function fetchCalendars(userId) {
	let calendars;
	await Promise.resolve(
		msGraphClient
			.api("users/" + userId + "/calendars")
			.get()
			.then((calendarsResponse) => {
				calendars = calendarsResponse.value.map(element => {
					return {
						id: element.id,
						name: element.name
					}
				});
			})
			.catch((error) => {
				console.log(error);
			}));

	return calendars;
}

async function fetchUsers() {
	let userId;

	await Promise.resolve(
		msGraphClient
			.api("/users?$filter=startswith(userPrincipalName,'business-calendar')&$select=id")
			.get()
			.then((usersResponse) => {
				userId = usersResponse.value.map(user => user.id)[0];
			})
			.catch((error) => {
				console.log(error);
			}));

	return userId;
}

let fetchHeyDueDateCalendars = async function (result) {
	let userId = await fetchUsers();

	let calendars = await fetchCalendars(userId);

	let events = await fetchEvents(userId, calendars);

	result.json(events);
}


export default ({ config, db }) => resource({

	/** GET / - List all entities */
	index({ params }, result) {
		fetchHeyDueDateCalendars(result);

		// calendarConverter();

		// result.json(calendarConverter());


	}

});