import dayjs from 'dayjs/esm';
import { ICar } from 'app/entities/car/car.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IInspection } from 'app/entities/inspection/inspection.model';
import { RepairStatus } from 'app/entities/enumerations/repair-status.model';

export interface IRepair {
  id: number;
  description?: string | null;
  repairStatus?: keyof typeof RepairStatus | null;
  dateCompleted?: dayjs.Dayjs | null;
  car?: ICar | null;
  employee?: IEmployee | null;
  inspection?: IInspection | null;
}

export type NewRepair = Omit<IRepair, 'id'> & { id: null };
