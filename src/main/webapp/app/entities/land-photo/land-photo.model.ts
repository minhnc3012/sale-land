import { ILand } from 'app/entities/land/land.model';

export interface ILandPhoto {
  id?: number;
  imageUrl?: string | null;
  land?: ILand | null;
}

export class LandPhoto implements ILandPhoto {
  constructor(public id?: number, public imageUrl?: string | null, public land?: ILand | null) {}
}

export function getLandPhotoIdentifier(landPhoto: ILandPhoto): number | undefined {
  return landPhoto.id;
}
