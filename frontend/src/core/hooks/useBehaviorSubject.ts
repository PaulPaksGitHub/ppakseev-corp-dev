import {BehaviorSubject} from "rxjs";
import {useEffect, useState} from "react";

export function useBehaviorSubject<T>(subject: BehaviorSubject<T>){
  const [value, setValue] = useState<T>(subject.getValue());

  useEffect(() => {
    const subscription = subject.subscribe({next: setValue});
    return () => {
      !subscription.closed && subscription.unsubscribe();
    }
  },[])

  return value;
}

export function useDataStream<T>(subject: BehaviorSubject<T>){
  return useBehaviorSubject<T>(subject);
}
