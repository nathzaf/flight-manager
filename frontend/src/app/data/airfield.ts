export interface Airfield {
  icaoCode: string;
  name: string;
  iataCode: string;
}

export type UpdateAirfieldRequest = Omit<Airfield, "iataCode">;
