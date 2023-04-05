using Microsoft.CodeAnalysis;
using Microsoft.CodeAnalysis.CSharp;
using Microsoft.CodeAnalysis.CSharp.Syntax;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using Newtonsoft.Json;
using System.IO;
using System;

namespace CBDetectorCSharp
{
    class PrintSyntaxTree
    {
        public class FileInfo
        {
            public string FileName;
            public string Text;
            public Node SyntaxTree;
            public FileInfo(string fileName, string text, Node node)
            {
                FileName = fileName;
                Text = text;
                SyntaxTree = node;
            }
        }

        public class Node
        {
            public string Label;
            public int Start;
            public int End;
            public List<Node> Children = new List<Node>();

            public Node(int start, int end)
            {
                this.Start = start;
                this.End = end;
            }

            public Node(SyntaxNode syntaxNode)
            {
                this.Label = syntaxNode.GetType().Name;
                this.Start = syntaxNode.Span.Start;
                this.End = syntaxNode.Span.End;

                foreach (SyntaxNode childSyntaxNode in syntaxNode.ChildNodes())
                {
                    Children.Add(new Node(childSyntaxNode));
                }
            }

            public void AddChild(Node child)
            {
                Children.Add(child);
            }

            public void Print(string src, string indent = "", bool isLast = true)
            {
                string stringPrefix = $"{indent}{(isLast ? "└── " : "├── ")}";
                string stringLabel = $"{(this.Label)}  ";
                string stringValue = src.Substring(this.Start, this.End - this.Start).Replace("\n", $"\n{indent}{(isLast ? " " : "│")}    {new string(' ', this.Label.ToString().Length)}┆");
                System.Console.WriteLine(stringPrefix + stringLabel + stringValue);
                for (int i = 0; i < this.Children.Count; i++)
                {
                    Node childNode = this.Children[i];
                    string childIndent = $"{indent}{(isLast ? "    " : "│   ")}";
                    childNode.Print(src, childIndent, (i == this.Children.Count - 1));
                }
            }
        }

        public static void Print(string file)
        {
            string text = File.ReadAllText(file);
            SyntaxTree tree = CSharpSyntaxTree.ParseText(text);
            Node node = new Node(tree.GetRoot());
            var stringifiedResult = JsonConvert.SerializeObject(node);
            Console.WriteLine(stringifiedResult);
        }
    }
}
