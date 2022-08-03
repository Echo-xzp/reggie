//package com.echoes;
//
//
///**
// * @Author : Xiaozp
// * @ClassName : CodeGenerator
// * @Description : TODO
// * @create : 2022/7/24 14:56
// * @Version : v1.0
// * @Powered By Corner Lab
// */
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.annotation.FieldFill;
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.PackageConfig;
//import com.baomidou.mybatisplus.generator.config.StrategyConfig;
//import com.baomidou.mybatisplus.generator.config.po.TableFill;
//import com.baomidou.mybatisplus.generator.config.rules.DateType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import org.apache.commons.lang.StringUtils;
//
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class CodeGenerator {
//
//
//    public static String scanner(String tip) {
//        Scanner scanner = new Scanner(System.in);
//        StringBuilder help = new StringBuilder();
//        help.append("请输入" + tip + "：");
//        System.out.println(help.toString());
//        if (scanner.hasNext()) {
//            String ipt = scanner.next();
//            if (StringUtils.isNotBlank(ipt)) {
//                return ipt;
//            }
//        }
//        throw new MybatisPlusException("请输入正确的" + tip + "！");
//    }
//
//
//    public static void main(String[] args) {
//        // 代码生成器
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "/src/main/java");//设置代码生成路径
//        gc.setFileOverride(true);//是否覆盖以前文件
//        gc.setOpen(false);//是否打开生成目录
//        gc.setAuthor("Xiao ZhiPeng");//设置项目作者名称
//        gc.setIdType(IdType.ASSIGN_ID);//设置主键策略
//        gc.setBaseResultMap(true);//生成基本ResultMap
//        gc.setBaseColumnList(true);//生成基本ColumnList
//        gc.setServiceName("%sService");//去掉服务默认前缀
////        gc.setMapperName("%sMapper");
////        gc.setControllerName("%sController");
//        gc.setDateType(DateType.TIME_PACK);//设置时间类型
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://localhost:3306/reggie?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8");
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        dsc.setUsername("root");
//        dsc.setPassword("123456");
//        mpg.setDataSource(dsc);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("com.echoes");
//        pc.setMapper("mapper");
//        pc.setXml("mapper.xml");
//        pc.setEntity("entity");
//        pc.setService("service");
//        pc.setServiceImpl("service.impl");
//        pc.setController("controller");
//        mpg.setPackageInfo(pc);
//
//        // 策略配置
//        StrategyConfig sc = new StrategyConfig();
//        sc.setNaming(NamingStrategy.underline_to_camel);
//        sc.setColumnNaming(NamingStrategy.underline_to_camel);
//        sc.setEntityLombokModel(true);//自动lomboka
//        sc.setRestControllerStyle(true);
//        sc.setControllerMappingHyphenStyle(true);
//
//        sc.setLogicDeleteFieldName("is_deleted");//设置逻辑删除
//
//        //设置自动填充配置
//        TableFill gmt_create_time = new TableFill("create_time", FieldFill.INSERT);
//        TableFill gmt_modified_time = new TableFill("update_time", FieldFill.INSERT_UPDATE);
//        TableFill gmt_create_user = new TableFill("create_user", FieldFill.INSERT);
//        TableFill gmt_modified_user = new TableFill("update_user", FieldFill.INSERT_UPDATE);
//        ArrayList<TableFill> tableFills = new ArrayList<>();
//        tableFills.add(gmt_create_time);
//        tableFills.add(gmt_modified_time);
//        tableFills.add(gmt_create_user);
//        tableFills.add(gmt_modified_user);
//        sc.setTableFillList(tableFills);
//
//        //乐观锁
//        sc.setVersionFieldName("version");
//        sc.setRestControllerStyle(true);//驼峰命名
//
//
////          sc.setTablePrefix("tbl_"); //设置表名前缀
//        sc.setInclude(scanner("表名，多个英文逗号分割").split(","));
//        mpg.setStrategy(sc);
//
//        // 生成代码
//        mpg.execute();
//    }
//
//}