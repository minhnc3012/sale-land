import { ILand } from 'app/entities/land/land.model';

export interface ILandCoordinate {
  id?: number;
  latitude?: number | null;
  longitude?: number | null;
  land?: ILand | null;
}

export class LandCoordinate implements ILandCoordinate {
  constructor(public id?: number, public latitude?: number | null, public longitude?: number | null, public land?: ILand | null) {}
}

export function getLandCoordinateIdentifier(landCoordinate: ILandCoordinate): number | undefined {
  return landCoordinate.id;
}
