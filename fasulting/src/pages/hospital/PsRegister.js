import Stepper from "../../components/User/Stepper";
import { Container } from "@mui/system";
import PsRegisterForm from "../../components/User/PsRegisterForm";

// const useStyles = makeStyles(() => ({
//   container: {
//     position: "absolute",
//     left: "0",
//     width: "100vw",
//     backgroundColor: "#E5F3F5",
//     display: "flex",
//     justifyContent: "center",
//     alignItems: "center",
//     minHeight: "100vh",
//   },
// }));
function Register() {
  // const classes = useStyles();
  return (
    <div>
      {/* <div className={classes.container}> */}
      <Container maxWidth="lg">
        <PsRegisterForm />
      </Container>
    </div>
  );
}

export default Register;