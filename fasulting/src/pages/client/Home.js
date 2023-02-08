import Banner from "../../components/Banner";
import MainCategoryList from "../../components/Category/MainCategoryList";
import { Container } from "@mui/system";
// import { useState } from "react";
// import axios from "axios";
import Footer from "../../components/Footer";

import { useEffect, useState } from "react";
import axios from "axios";
import axiosAPi from "../../api/axiosApi";

function Home() {
  //통신 테스트
  // axios.get("/main").then((res) => {
  //   console.log(res);
  // });
  // const [maincategory, setMaincategory] = useState([]);
  // useEffect(() => {
  //   axiosApi.get("/main").then((res) => {
  //     setMaincategory(res.data.responseObj);
  //   });
  // }, []);

  const [maincategory, setMaincategory] = useState([]);
  useEffect(() => {
    axiosAPi.get("/main").then((res) => {
      console.log(res.data);
      setMaincategory(res.data.responseObj);
    });
  }, []);
  return (
    <div>
      <Container>
        <Banner />
      </Container>
      <Container maxidth="lg">
        <MainCategoryList maincategory={maincategory} />
      </Container>
      <Footer />
    </div>
  );
}

export default Home;
