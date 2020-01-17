export interface IActor {
  actorId: number;
  actorName: string | null;
  actorTypeName: string | null;
  actorTypeId: number | null;
  relationshipTypeId: number;
}
