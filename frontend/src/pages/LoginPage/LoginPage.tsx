import * as React from 'react';
import {useState} from "react";
import AuthStore from "../../core/stores/AuthStore";
import Navigation from "../../core/modules/Navigation";
import {Box, Button, Container, Grid, TextField} from "@material-ui/core";
import SharedNoteItem from "../../modules/ShareNoteItem";

interface IProps {

}

const LoginPage: React.FunctionComponent<IProps> = props => {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");


  const onPressLogin = () => {
    AuthStore.login({login, password})
      .then(() => {
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
        <Box m={2}>
          <Button
            onClick={onPressLogin}
            variant="contained"
            color={"primary"}
            disabled={login.length === 0 || password.length === 0}
          >
            Войти
          </Button>
        </Box>
      </Grid>
    </Container>
  )
}

export default LoginPage;
