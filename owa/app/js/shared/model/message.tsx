import User from './user';
import ActorSchedule from './actor-schedule';

export default class Message {
  type: string;
  author: User;
  actorSchedule: ActorSchedule;
}
