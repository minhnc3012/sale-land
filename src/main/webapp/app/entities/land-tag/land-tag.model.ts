import { ILand } from 'app/entities/land/land.model';

export interface ILandTag {
  id?: number;
  tag?: string | null;
  land?: ILand | null;
}

export class LandTag implements ILandTag {
  constructor(public id?: number, public tag?: string | null, public land?: ILand | null) {}
}

export function getLandTagIdentifier(landTag: ILandTag): number | undefined {
  return landTag.id;
}
