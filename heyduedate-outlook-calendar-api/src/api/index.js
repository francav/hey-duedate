import { version } from '../../package.json';
import { Router } from 'express';
import calendars from './calendars';

export default ({ config, db }) => {
	let api = Router();

	// mount the facets resource
	api.use('/calendars', calendars({ config, db }));

	// perhaps expose some API metadata at the root
	api.get('/', (req, res) => {
		res.json({ version });
	});

	return api;
}
