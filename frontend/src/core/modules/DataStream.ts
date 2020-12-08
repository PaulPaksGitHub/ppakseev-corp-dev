import {BehaviorSubject} from "rxjs";

export type Pending = 'clear' | 'loading' | 'done' | 'failed' | 'loading_more' | 'refreshing';

type PendingValue<P> = {
  pending: Pending | P;
}

type DataValue<T> = {
  data: T,
}

export type DataStreamValue<T, P = Pending> = PendingValue<P> & DataValue<T>;

export class DataStream<T, P = Pending> extends BehaviorSubject<DataStreamValue<T, P>> {
  private _defaultValue: DataStreamValue<T, P>;

  constructor(props: T, initialPending?: P) {
    super({
      data: props,
      pending: initialPending || "clear",
    });
    this._defaultValue = {
      data: props,
      pending: (initialPending || "clear") as P,
    };
  }

  public clear = () => {
    this.next(this._defaultValue);
  }

  public update = (data: PendingValue<P> | DataValue<T> | DataStreamValue<T, P>) => {
    this.next({
      ...this.getValue(),
      ...data,
    })
  }
}
