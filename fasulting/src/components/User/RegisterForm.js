import React from "react";
import { useFormik } from "formik";
import * as yup from "yup";
import { Typography, TextField, Button, Link, Checkbox } from "@mui/material";
import "react-phone-number-input/style.css";
import styles from "./Form.module.css";
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/bootstrap.css";

const validationSchema = yup.object({
  email: yup
    .string()
    .email("올바른 이메일 형식이 아닙니다.")
    .required("이메일을 입력해주세요."),
  password: yup
    .string()
    .matches(
      /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/,
      "숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요."
    )
    .required("비밀번호를 입력해주세요."),
  repassword: yup
    .string()
    .oneOf([yup.ref("password")], "비밀번호가 일치하지 않습니다.")
    .required("비밀번호를 재입력해주세요."),
  name: yup
    .string()
    .matches(/^[가-힣a-zA-Z]+$/, "올바른 이름을 입력해주세요.")
    .required("이름을 입력해주세요."),
  birth: yup
    .string()
    .matches(
      /^(19[0-9][0-9]|20\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$/,
      "생년월일 8자리를 입력해주세요."
    )
    .required("생년월일을 입력해주세요."),
  phone: yup
    .string()
    .matches(/^[0-9]{2,3}[0-9]{3,4}[0-9]{4}/, "올바른 번호를 입력해주세요.")
    .required("전화번호를 입력해주세요."),
  agreement: yup
    .bool()
    .oneOf([true], "You need to accept the terms and conditions"),
});

export default function RegisterForm() {
  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
      repassword: "",
      name: "",
      birth: "",
      phone: "",
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      console.log(values);
    },
  });

  return (
    <>
      <div className={styles.formwrapper}>
        <form onSubmit={formik.handleSubmit}>
          <div className={styles.inputForm}>
            <div className={styles.inputItem}>
              <div className={styles.labelFirst}>이메일</div>
              <TextField
                fullWidth
                placeholder="id@fasulting.com"
                name="email"
                value={formik.values.email}
                onChange={formik.handleChange}
                error={formik.touched.email && Boolean(formik.errors.email)}
                helperText={formik.touched.email ? formik.errors.email : ""}
              />
            </div>
            <div className={styles.inputItem}>
              <div className={styles.label}>비밀번호</div>
              <TextField
                fullWidth
                name="password"
                placeholder="숫자+영문자+특수문자 조합으로 8자리 이상"
                type="password"
                value={formik.values.password}
                onChange={formik.handleChange}
                error={
                  formik.touched.password && Boolean(formik.errors.password)
                }
                helperText={
                  formik.touched.password ? formik.errors.password : ""
                }
              />
            </div>
            <div className={styles.inputItem}>
              <div className={styles.label}>비밀번호 확인</div>
              <TextField
                fullWidth
                id="repassword"
                name="repassword"
                placeholder="비밀번호를 다시 입력해주세요."
                type="password"
                value={formik.values.repassword}
                onChange={formik.handleChange}
                error={
                  formik.touched.repassword && Boolean(formik.errors.repassword)
                }
                helperText={
                  formik.touched.repassword ? formik.errors.repassword : ""
                }
              />
            </div>
            <div className={styles.inputItem}>
              <div className={styles.label}>이름</div>
              <TextField
                fullWidth
                id="name"
                name="name"
                placeholder="이름을 입력해주세요."
                type="text"
                value={formik.values.name}
                onChange={formik.handleChange}
                error={formik.touched.name && Boolean(formik.errors.name)}
                helperText={formik.touched.name ? formik.errors.name : ""}
              />
            </div>
            <div className={styles.inputItem}>
              <div className={styles.label}>생년월일</div>
              <TextField
                fullWidth
                id="birth"
                name="birth"
                placeholder="YYYYMMDD"
                value={formik.values.birth}
                onChange={formik.handleChange}
                error={formik.touched.birth && Boolean(formik.errors.birth)}
                helperText={formik.touched.birth ? formik.errors.birth : ""}
              />
            </div>
            {/* <div className={styles.inputItem}>
              <div className={styles.label}>생년월일</div>
              <PhoneInput country="ko" />
              </div> */}
            <div className={styles.inputItem}>
              <div className={styles.label}>휴대폰 번호</div>
              {/* <TextField
                fullWidth
                id="phone"
                name="phone"
                placeholder="하이픈(-) 없이 입력해주세요."
                type="text"
                value={formik.values.phone}
                onChange={formik.handleChange}
                error={formik.touched.phone && Boolean(formik.errors.phone)}
                helperText={formik.touched.phone ? formik.errors.phone : ""}
              /> */}
              <PhoneInput
                inputStyle={{
                  width: "464px",
                  "&:focus": {
                    borderColor: "#03b2cb",
                  },
                }}
                fullWidth="true"
                placeholder="(+82) 010-1234-5678"
                defaultCountry="so"
                value={formik.values.phone}
                onChange={formik.handleChange}
              />
            </div>
            <div className={styles.inputItem}>
              <div className={`${styles.label} ${styles.labelcolor}`}>
                개인정보 제공 동의
              </div>
              <div>
                <Checkbox
                  name="agreement"
                  error={
                    formik.touched.agreement && Boolean(formik.errors.agreement)
                  }
                  helperText={
                    formik.touched.agreement ? formik.errors.agreement : ""
                  }
                />
                <span>
                  예약 진행, 고객 상담, 고객 관리 및 고객 문의를 위해 예약자
                  이름, 예약자 휴대폰 번호를 수집하여 해당 병원 업체에 제공함에
                  동의합니다.
                </span>
              </div>
            </div>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              size="large"
            >
              회원가입
            </Button>
            <Link
              href="/psregist"
              variant="body2"
              className={styles.flexCenter}
            >
              의사로 회원가입 하시나요?
            </Link>
          </div>
        </form>
      </div>
    </>
  );
}
