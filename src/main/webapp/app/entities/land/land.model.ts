import { ILandTag } from 'app/entities/land-tag/land-tag.model';
import { ILandPhoto } from 'app/entities/land-photo/land-photo.model';
import { ILandCoordinate } from 'app/entities/land-coordinate/land-coordinate.model';
import { LandStatus } from 'app/entities/enumerations/land-status.model';
import { LandType } from 'app/entities/enumerations/land-type.model';
import { UnitPriceType } from 'app/entities/enumerations/unit-price-type.model';
import { PriceType } from 'app/entities/enumerations/price-type.model';
import { FeeType } from 'app/entities/enumerations/fee-type.model';

export interface ILand {
  id?: number;
  title?: string | null;
  address?: string | null;
  status?: LandStatus | null;
  type?: LandType | null;
  price?: number | null;
  init?: UnitPriceType | null;
  priceType?: PriceType | null;
  feeType?: FeeType | null;
  description?: string | null;
  width?: number | null;
  height?: number | null;
  area?: number | null;
  latitude?: number | null;
  longitude?: number | null;
  tags?: ILandTag[] | null;
  photos?: ILandPhoto[] | null;
  coordinates?: ILandCoordinate[] | null;
}

export class Land implements ILand {
  constructor(
    public id?: number,
    public title?: string | null,
    public address?: string | null,
    public status?: LandStatus | null,
    public type?: LandType | null,
    public price?: number | null,
    public init?: UnitPriceType | null,
    public priceType?: PriceType | null,
    public feeType?: FeeType | null,
    public description?: string | null,
    public width?: number | null,
    public height?: number | null,
    public area?: number | null,
    public latitude?: number | null,
    public longitude?: number | null,
    public tags?: ILandTag[] | null,
    public photos?: ILandPhoto[] | null,
    public coordinates?: ILandCoordinate[] | null
  ) {}
}

export function getLandIdentifier(land: ILand): number | undefined {
  return land.id;
}
