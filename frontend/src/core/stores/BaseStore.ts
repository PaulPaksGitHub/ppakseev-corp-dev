import axios, {AxiosInstance} from "axios";
import Navigation from "../modules/Navigation";

export default class BaseStore {
  public makeRequest = (): AxiosInstance => {
    const accessToken = localStorage.getItem("@access");

    const inst = axios.create({
      baseURL: 'http://localhost:8080/api/',
      headers: {
        "access-token": accessToken,
      }
    });

    inst.interceptors.response.use(r => r, async error => {
      if (error.response && error.response.status === 401) {
        const accessToken = localStorage.getItem("@access");
        const refreshToken = localStorage.getItem("@refresh");

        if (accessToken && refreshToken) {
          const response = await this.makeRequest().post('refresh-token', {
            foolAccessToken: accessToken,
            refreshToken
          });

          if (response.data.accessToken) {
            localStorage.setItem("@access", response.data.accessToken);
            localStorage.setItem("@user_id", response.data.user_id);

            return await axios.request({
              ...error.config,
              headers: {
                ...error.config.headers,
                "access-token": response.data.accessToken,
              }
            })
          } else {
            localStorage.removeItem("@access");
            localStorage.removeItem("@refresh");
            localStorage.removeItem("@user_id");
          }
        } else {
          Navigation.push("/login");
        }
      }
    })

    return inst;
  }
}
