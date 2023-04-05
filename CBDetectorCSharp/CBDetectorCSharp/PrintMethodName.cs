using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using Microsoft.CodeAnalysis;
using Microsoft.CodeAnalysis.CSharp;
using Microsoft.CodeAnalysis.CSharp.Syntax;
using Microsoft.CodeAnalysis.Text;

namespace HelloSyntaxTree
{
    class PrintMethodName
    {
        public static void Print(string root =  "D:\\code\\java-csharp-ast\\src\\lucenenet-master\\")
        {
            List<string> allFiles = new List<string>();
            void iter(string path)
            {
                var files = from file in Directory.EnumerateFiles(path) select file;
                foreach (var file in files)
                {
                    if (file.EndsWith(".cs"))
                    {
                        allFiles.Add(file);
                        Console.WriteLine("file: " + file);
                    }
                }

                var folders = from folder in Directory.EnumerateDirectories(path) select folder;
                foreach (var folder in folders)
                {
                    iter(folder);
                }
            }
            iter(root);

            Dictionary<string, List<string>> result = new Dictionary<string, List<string>>();
            foreach (var file in allFiles)
            {

                Console.WriteLine("parse: " + file);
                List<string> methods = new List<string>();
                SyntaxTree tree = CSharpSyntaxTree.ParseText(File.ReadAllText(file));
                foreach (var node in tree.GetRoot().DescendantNodes())
                {
                    switch (node)
                    {
                        case MethodDeclarationSyntax _:
                            var methodName = (node as MethodDeclarationSyntax).Identifier.Text;
                            Console.WriteLine("method: " + methodName);
                            methods.Add(methodName);
                            break;
                        default:
                            break;
                    }
                }
                var formattedFileName = file.Replace(root, "").Replace("\\", "/");
                result.Add(formattedFileName, methods);
            }
            var stringifiedResult = JsonSerializer.Serialize(result);
            Console.WriteLine(stringifiedResult);
            File.WriteAllText("MethodName.txt", stringifiedResult);
            Console.ReadKey();
        }
    }
}
