"use client";
import {
  GithubFilled,
  LogoutOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import { ProLayout } from "@ant-design/pro-components";
import { Dropdown, Input, theme } from "antd";
import React from "react";
import Image from "next/image";
import { usePathname } from "next/navigation";
import Link from "next/link";
import GlobalFooter from "@/components/GlobalFooter";
import "./index.css";
import menus from "../../../config/menu";
import { listQuestionBankVoByPageUsingPost } from "@/api/questionBankController";

const SearchInput = () => {
  const { token } = theme.useToken();
  return (
    <div
      key="SearchOutlined"
      aria-hidden
      style={{
        display: "flex",
        alignItems: "center",
        marginInlineEnd: 24,
      }}
      onMouseDown={(e) => {
        e.stopPropagation();
        e.preventDefault();
      }}
    >
      <Input
        style={{
          borderRadius: 4,
          marginInlineEnd: 12,
          backgroundColor: token.colorBgTextHover,
        }}
        prefix={
          <SearchOutlined
            style={{
              color: token.colorTextLightSolid,
            }}
          />
        }
        placeholder="搜索题目"
        variant="borderless"
      />
    </div>
  );
};

interface Props {
  children?: React.ReactNode;
}

export default function BasicLayout({ children }: Props) {
  listQuestionBankVoByPageUsingPost({}).then((res) => {
    console.log(res);
  });
  const pathname = usePathname();
  return (
    <div
      id="basicLayout"
      style={{
        height: "100vh",
        overflow: "auto",
      }}
    >
      <ProLayout
        layout={"top"}
        title={"面试鸭刷题平台"}
        logo={
          <Image
            src="/assets/logo.svg"
            height={32}
            width={32}
            alt={"面试鸭刷题平台"}
          />
        }
        location={{
          pathname,
        }}
        token={{
          header: {
            colorBgMenuItemSelected: "rgba(0,0,0,0.04)",
          },
        }}
        siderMenuType="group"
        menu={{
          collapsedShowGroupTitle: true,
        }}
        avatarProps={{
          src: "/assets/logo.svg",
          size: "small",
          title: "sstdl",
          render: (props, dom) => {
            return (
              <Dropdown
                menu={{
                  items: [
                    {
                      key: "logout",
                      icon: <LogoutOutlined />,
                      label: "退出登录",
                    },
                  ],
                }}
              >
                {dom}
              </Dropdown>
            );
          },
        }}
        // 操作渲染
        actionsRender={(props) => {
          if (props.isMobile) return [];
          return [
            <SearchInput key="search" />,
            <a
              key="github"
              href="https://github.com/sstdl/mianshiya-sstdl"
              target="_blank"
            >
              <GithubFilled key="GithubFilled" />
            </a>,
          ];
        }}
        // 标题渲染
        headerTitleRender={(logo, title, _) => {
          return (
            <a href="https://www.mianshiya.com" target="_blank">
              {logo}
              {title}
            </a>
          );
        }}
        // 渲染底部栏
        footerRender={() => <GlobalFooter />}
        onMenuHeaderClick={(e) => console.log(e)}
        menuDataRender={() => {
          return menus;
        }}
        // 定义菜单项如何渲染
        menuItemRender={(item, dom) => (
          <Link href={item.path || "/"} target={item.target}>
            {dom}
          </Link>
        )}
      >
        {children}
      </ProLayout>
    </div>
  );
}
