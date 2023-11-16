CREATE TABLE product_tbl (

	
    prd_code VARCHAR2(30), 
    prd_num NUMBER NOT NULL, 
    prd_name VARCHAR2(70) NOT NULL, 
    prd_price VARCHAR2(10), 
    prd_img VARCHAR2(60),
    img_path VARCHAR2(100),
    prd_company VARCHAR2(20)
     
    CONSTRAINT CNO_PK PRIMARY KEY(CNO) 
);