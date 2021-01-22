import BaseStore from "./BaseStore";
import {IRegisterForm} from "./interfaces/IRegisterForm";
import {ITokens} from "./interfaces/ITokens";
import {ILoginForm} from "./interfaces/ILoginForm";
import Navigation from "../modules/Navigation";

class _AuthStore extends BaseStore {
  public register = async (options: IRegisterForm): Promise<ITokens> => {
    try {
      const response = await this.makeRequest().post('register', options);

      localStorage.setItem("@access", response.data.accessToken);
      localStorage.setItem("@refresh", response.data.refreshToken);
      localStorage.setItem("@user_id", response.data.user_id);

      return response.data;
    } catch (e) {
      console.error(e);
      throw new Error(e);
    }
  }

  public login = async (options: ILoginForm): Promise<ITokens> => {
    try {
      const response = await this.makeRequest().post('login', options);

      console.log("TOKENS", response.data);

      localStorage.setItem("@access", response.data.accessToken);
      localStorage.setItem("@refresh", response.data.refreshToken);
      localStorage.setItem("@user_id", response.data.user_id);

      return response.data;
    } catch (e) {
      console.error(e);
      throw new Error(e);
    }
  }
}

const AuthStore = new _AuthStore();

export default AuthStore;
