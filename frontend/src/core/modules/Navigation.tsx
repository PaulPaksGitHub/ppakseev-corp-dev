import* as React from "react";
import {useDataStream} from "../hooks/useBehaviorSubject";
import {DataStream} from "./DataStream";


class _Navigation {
  public currentPage = new DataStream<string>("/");

  private hist: string[] = [];

  public push = (path: string) => {
    this.currentPage.update({data: path});
    this.hist.push(path);
  }
}

const Navigation = new _Navigation();

export default Navigation;

export const Route: React.FunctionComponent<any> = props => {
  const currpage = useDataStream(Navigation.currentPage);
  if (currpage.data === props.path) {
    return props.children;
  }
  return null;
}
