import outlookCalendar from './outlook-calendar.json'

export default () => {
    return outlookCalendar.map(o => {
        { return o.events }
    }).filter(o => o.subject === 'weekend');
    
}