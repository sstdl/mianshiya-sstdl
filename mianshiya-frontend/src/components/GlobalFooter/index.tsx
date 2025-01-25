"use client";
import React from "react";
import "./index.css";

export default function GlobalFooter() {
  const currentYear = new Date().getFullYear();
  return (
    <div className="global-footer">
      <div>© {currentYear} 面试刷题平台</div>
      <div>
        <a href="https://github.com/sstdl" target="_blank">
          sstdl
        </a>
      </div>
    </div>
  );
}
