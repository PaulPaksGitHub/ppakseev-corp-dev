import * as React from "react";
import {useState} from "react";
import {Box, Button, Container, Grid, TextField} from "@material-ui/core";
import AuthStore from "../../core/stores/AuthStore";
import Navigation from "../../core/modules/Navigation";

interface IProps {

}

const RegisterPage: React.FunctionComponent<IProps> = props => {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [confirm_password, setConfirmPassword] = useState("");


  const onPressRegister = () => {
    AuthStore.register({login, password, confirm_password})
      .then(() => {
        console.log("PUSH");
        Navigation.push("/notes");
      })
  };

  return (
    <Container>
      <Grid
        container
        direction="column"
        justify="center"
        alignItems="center"
        spacing={2}
      >
        <TextField
          id="standard-basic"
          label="Логин"
          value={login}
          onChange={e => {
            setLogin(e.target.value)
          }}
        />
        <TextField
          id="standard-basic"
          label="Пароль"
          value={password}
          onChange={e => {
            setPassword(e.target.value)
          }}
        />
        <TextField
          id="standard-basic"
          label="Пароль"
          value={confirm_password}
          onChange={e => {
            setConfirmPassword(e.target.value)
          }}
        />
        {password !== confirm_password ? (
          <p>пароли не совпадают</p>
        ) : null}
        <Box m={2}>
          <Button
            onClick={onPressRegister}
            variant="contained"
            color={"primary"}
            disabled={login.length === 0 || password.length === 0 || confirm_password.length === 0 || password !== confirm_password}
          >
            Зарегистрироваться
          </Button>
        </Box>
        <Box m={2}>
          <Button
            onClick={() => Navigation.push("/login")}
            variant="contained"
            color={"primary"}
          >
            Уже есть аккаунт
          </Button>
        </Box>
      </Grid>
    </Container>
  )
}

export default RegisterPage;
